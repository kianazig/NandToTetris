import java.util.HashMap;

public class SymbolTable {
	
	HashMap<String, Symbol> localSymbols;//which one
	HashMap<String, Symbol> globalSymbols;//which one 
	int staticCount;
	int fieldCount;
	int argumentCount;
	int localCount;
	
	//TODO: handle nested scoping
	
	enum Kind { STATIC, FIELD, ARG, VAR };

	public SymbolTable() {
		localSymbols = new HashMap<String, Symbol>();
		globalSymbols = new HashMap<String, Symbol>();
		staticCount = fieldCount = argumentCount = localCount = 0;
	}
	
	public void startSubroutine() {
		localSymbols.clear();
		argumentCount = localCount = 0;
	}
	
	public void define(String name, String type, int kind) {
		if (kind == 0) {
			Symbol nextSymbol = new Symbol(name, type, kind, staticCount);
			globalSymbols.put(nextSymbol.name, nextSymbol);
			staticCount++;
		}
		else if (kind == 1) {
			Symbol nextSymbol = new Symbol(name, type, kind, fieldCount);
			globalSymbols.put(nextSymbol.name, nextSymbol);
			fieldCount++;
		}
		else if (kind == 2) {
			Symbol nextSymbol = new Symbol(name, type, kind, argumentCount);
			globalSymbols.put(nextSymbol.name, nextSymbol);
			argumentCount++;
		}
		else if (kind == 3) {
			Symbol nextSymbol = new Symbol(name, type, kind, localCount);
			globalSymbols.put(nextSymbol.name, nextSymbol);
			localCount++;
		}
	}
	
	public int VarCount(int kind) {
		if (kind == 0) {
			return staticCount;
		}
		else if (kind == 1) {
			return fieldCount;
		}
		else if (kind == 2) {
			return argumentCount;
		}
		else if (kind == 3) {
			return localCount;
		}
		else {
			return -1;
		}
	}
	
	public int KindOF(String name) {
		if (globalSymbols.containsKey(name)) {
			return globalSymbols.get(name).kind;
		}
		else if (localSymbols.containsKey(name)) {
			return localSymbols.get(name).kind;
		}
		return -1;
	}
	
	public String TypeOf(String name) {
		if (globalSymbols.containsKey(name)) {
			return globalSymbols.get(name).type;
		}
		else if (localSymbols.containsKey(name)) {
			return localSymbols.get(name).type;
		}
		return "";
	}
	
	public int IndexOf(String name) {
		if (globalSymbols.containsKey(name)) {
			return globalSymbols.get(name).index;
		}
		else if (localSymbols.containsKey(name)) {
			return localSymbols.get(name).index;
		}
		return -1;
	}
	
	private class Symbol{
		public String name;
		public String type;
		public int kind;
		public int index;
		
		public Symbol(String name, String type, int kind, int index) {
			this.name = name;
			this.type = type;
			this.kind = kind;
			this.index = index;
		}
	}
	
}
